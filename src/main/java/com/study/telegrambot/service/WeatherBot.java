package com.study.telegrambot.service;

import com.study.telegrambot.model.WeatherModel;
import com.study.telegrambot.util.Config;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeatherBot extends TelegramLongPollingBot {

    private static final String BOT_USERNAME_PROP_TITLE = "telegrambot.username";
    private static final String BOT_TOKEN_PROP_TITLE = "telegrambot.token";

    public static void init() {
        System.out.println("Init");
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new WeatherBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        System.out.println("name");
        return Config.get(BOT_USERNAME_PROP_TITLE);
    }

    @Override
    public String getBotToken() {
        System.out.println("token");
        return Config.get(BOT_TOKEN_PROP_TITLE);
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        WeatherModel weatherModel = new WeatherModel();

        if (message.hasText()) {
            switch (message.getText()) {
                case "/help":
                    sendMessage(message, "How can I help you?");
                    break;
                case "/settings":
                    sendMessage(message, "What are we going to configure?");
                    break;
                default:
                    try {
                        sendMessage(message, WeatherService.getWeather(message.getText(), weatherModel));
                    } catch (IOException e) {
                        sendMessage(message, "City not found!");
                    }
            }
        }
    }

    private void sendMessage(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);

        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            sendMessage(message, "Failed to process request!");
        }
    }

    private void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/settings"));
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
}
