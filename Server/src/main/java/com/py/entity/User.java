package com.py.entity;

import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @Author py
 * @Date 2024/11/22
 */
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class User {
    String id;
    String username;
    String isOnline;
}
