package org.bot;

import com.github.kagkarlsson.scheduler.Scheduler;
import com.github.kagkarlsson.scheduler.task.helper.RecurringTask;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import com.github.kagkarlsson.scheduler.task.schedule.FixedDelay;
import org.bot.bot.Bot;
import org.bot.command.data.ChatData;
import org.bot.enumerable.ChatPlatform;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Properties;

public class DatabaseScheduler {
    private final Scheduler scheduler;

    private final Bot telegramBot;

    private final Bot vkBot;

    private final RecurringTask<ChatData> subscribeTask;

    public DatabaseScheduler(Bot telegramBot, Bot vkBot) {

        this.telegramBot = telegramBot;
        this.vkBot = vkBot;
        //TODO change delay
        this.subscribeTask = Tasks.recurring("subscribeTask", FixedDelay.ofSeconds(10), ChatData.class)
                .execute(((taskInstance, executionContext) -> {
                    ChatData data = taskInstance.getData();

                    System.out.println("Отправка в " + data.chatPlatform());
                    System.out.println("Id = " + data.chatPlatform().toString() + "_" + data.chatId());
                    System.out.println(executionContext.getExecution().taskInstance + " = TASKINSTANCE");
                    // TODO:
                    // взять chatPlatform, определить в какого бота отправлять
                    // Вызвать JokeCommand(null, chatId, chatPlatform)
                    // send message в нужного бота
                }));

        PGSimpleDataSource pgSimpleDataSource = getPGDataSource();
        this.scheduler = Scheduler.create(pgSimpleDataSource, this.subscribeTask).build();
        scheduler.schedule(this.subscribeTask.instance("TEST TASK", new ChatData(ChatPlatform.VK, 111L)),
                Instant.now().plusSeconds(5));

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
