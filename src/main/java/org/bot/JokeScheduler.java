package org.bot;

import com.github.kagkarlsson.scheduler.Scheduler;
import com.github.kagkarlsson.scheduler.task.TaskInstanceId;
import com.github.kagkarlsson.scheduler.task.helper.RecurringTask;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import com.github.kagkarlsson.scheduler.task.schedule.FixedDelay;
import org.bot.bot.Bot;
import org.bot.command.data.ChatData;
import org.bot.service.JokeService;
import org.bot.service.RatingService;
import org.bot.util.DBConfigUtils;
import org.postgresql.ds.PGSimpleDataSource;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

/**
 * Планировщик отправки анекдотов по расписанию
 */
public class JokeScheduler {
    private final Scheduler scheduler;
    private final RecurringTask<ChatData> subscribeTask;

    public JokeScheduler(Map<String, Bot> botMap, JokeService jokeService, RatingService ratingService) {
        this.subscribeTask = Tasks.recurring("subscribeTask", FixedDelay.ofHours(24), ChatData.class)
                .execute(((taskInstance, executionContext) -> {

                    ChatData data = taskInstance.getData();
                    Long chatId = data.chatId();
                    String chatPlatform = data.chatPlatform();

                    Bot bot = botMap.get(chatPlatform);

                    if (bot == null) {
                        throw new RuntimeException("Бота для отправки не существует");
                    }

                    Joke joke = jokeService.getRandomJoke();
                    if (joke == null) {
                        bot.sendMessage(chatId, "Не найдено анекдотов для ежедневной отправки");
                        return;
                    }

                    jokeService.saveLastJoke(chatId, joke.getId());

                    Optional<Double> averageRating = ratingService.getAverageRatingForJoke(joke.getId());

                    String ratingString = averageRating.isPresent()
                            ? "\nРейтинг анекдота: " + averageRating.get()
                            : "";

                    bot.sendMessageWithRateKeyboard(chatId, "Анекдот дня: \n\n" + "Анекдот №" + joke.getId() +
                            "\n" + joke.getText() + ratingString);
                }));

        PGSimpleDataSource pgSimpleDataSource = new DBConfigUtils().getPGDataSource();
        this.scheduler = Scheduler.create(pgSimpleDataSource, subscribeTask).build();

        scheduler.start();
    }

    /**
     * Запланировать ежедневную отправку анекдота
     *
     * @param chatPlatform чат платформа
     * @param chatId       id чата
     * @param instant      время отправки
     */
    public void schedule(String chatPlatform, Long chatId, Instant instant) {

        String id = chatPlatform + "_" + chatPlatform;
        TaskInstanceId taskInstanceId = TaskInstanceId.of("subscribeTask", id);

        if (this.scheduler.getScheduledExecution(taskInstanceId).isPresent()) {
            this.scheduler.reschedule(this.subscribeTask.instance(id, new ChatData(chatPlatform, chatId)), instant);
        } else {
            this.scheduler.schedule(this.subscribeTask.instance(id, new ChatData(chatPlatform, chatId)), instant);
        }

    }

    /**
     * Отменить ежедневную отправку анекдотов в чате
     *
     * @param chatPlatform чат платформа
     * @param chatId       id чата
     */
    public void deschedule(String chatPlatform, Long chatId) {

        String id = chatPlatform + "_" + chatId;
        TaskInstanceId taskInstanceId = TaskInstanceId.of("subscribeTask", id);

        if (this.scheduler.getScheduledExecution(taskInstanceId).isPresent()) {
            this.scheduler.cancel(taskInstanceId);
        }
    }


}
