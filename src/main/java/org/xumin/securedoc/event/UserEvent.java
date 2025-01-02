package org.xumin.securedoc.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.xumin.securedoc.entity.UserEntity;
import org.xumin.securedoc.enumeration.EventType;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class UserEvent {
    private UserEntity user;
    private EventType eventType;
    private Map<?,?> data;
}
