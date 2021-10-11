package com.giftbook.giftBook.transport.templates;

import com.giftbook.giftBook.exceptions.ContentCreationException;

public interface NotificationContent {
    String getContent(Object... object) throws ContentCreationException;
}