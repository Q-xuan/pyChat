package com.py.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @Author py
 * @Date 2024/11/22
 */
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ChatChannel {
    String id;
    String name;
    String description;
}
