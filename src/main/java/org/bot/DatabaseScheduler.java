package org.bot;

import com.github.kagkarlsson.scheduler.ScheduledExecution;
import com.github.kagkarlsson.scheduler.Scheduler;
import com.github.kagkarlsson.scheduler.task.helper.RecurringTask;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import com.github.kagkarlsson.scheduler.task.schedule.FixedDelay;
import org.bot.bot.Bot;
import org.bot.command.JokeCommand;
import org.bot.command.data.ChatData;
import org.bot.enumerable.ChatPlatform;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Properties;

public class DatabaseScheduler {
    private final Scheduler scheduler;
    private final RecurringTask<ChatData> subscribeTask;

    public DatabaseScheduler(Bot telegramBot, Bot vkBot, JokeCommand jokeCommand) {

        this.subscribeTask = Tasks.recurring("subscribeTask", FixedDelay.ofHours(24), ChatData.class)
                .execute(((taskInstance, executionContext) -> {

                    ChatData data = taskInstance.getData();
                    Long chatId = data.chatId();
                    ChatPlatform chatPlatform = data.chatPlatform();

                    if (chatPlatform.equals(ChatPlatform.TELEGRAM)) {
                        telegramBot.sendMessage(chatId, jokeCommand.execute(null, chatId, chatPlatform));
                    } else if (chatPlatform.equals(ChatPlatform.VK)) {
                        vkBot.sendMessage(chatId, jokeCommand.execute(null, chatId, chatPlatform));
                    }
                }));

        PGSimpleDataSource pgSimpleDataSource = getPGDataSource();
        this.scheduler = Scheduler.create(pgSimpleDataSource, subscribeTask).build();

        scheduler.start();
    }

    public void schedule(ChatData chatData, Instant instant) {
        this.scheduler.schedule(
                this.subscribeTask
                        .instance(
                                chatData.chatPlatform().toString() + "_" + chatData.chatId(),
                                chatData
                        ),
                instant);
    }

    public void deschedule(ChatData chatData) {
        List<ScheduledExecution<ChatData>> tasks = this.scheduler.getScheduledExecutionsForTask("subscribeTask", ChatData.class);
        List<ScheduledExecution<ChatData>> currentChatTasks = tasks.stream()
                .filter(e -> e
                        .getTaskInstance()
                        .getId()
                        .contains(chatData.chatPlatform().toString() + "_" + chatData.chatId()))
                .toList();

        for (ScheduledExecution<ChatData> scheduledExecution : currentChatTasks) {
            this.scheduler.cancel(scheduledExecution.getTaskInstance());
        }
    }

    private PGSimpleDataSource getPGDataSource() {
        try (InputStream input = new FileInputStream("src/main/resources/connection.properties")) {

            Properties properties = new Properties();
            properties.load(input);

            PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
            pgSimpleDataSource.setDatabaseName(properties.getProperty("db.name"));
            pgSimpleDataSource.setPortNumber(Integer.parseInt(properties.getProperty("db.port")));
            pgSimpleDataSource.setUser(properties.getProperty("db.user"));
            pgSimpleDataSource.setPassword(properties.getProperty("db.password"));

            return pgSimpleDataSource;

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Не удалось создать планировщик событий: проверьте наличие конфигурационного файла");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
