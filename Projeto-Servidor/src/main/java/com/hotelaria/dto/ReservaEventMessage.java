package com.hotelaria.dto;

import java.io.Serializable;

public class ReservaEventMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long reservaId;
    private String eventType;

    public ReservaEventMessage() {}

    public ReservaEventMessage(Long reservaId, String eventType) {
        this.reservaId = reservaId;
        this.eventType = eventType;
    }

    public static ReservaEventMessageBuilder builder() {
        return new ReservaEventMessageBuilder();
    }

    public Long getReservaId() { return reservaId; }
    public void setReservaId(Long reservaId) { this.reservaId = reservaId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    @Override
    public String toString() {
        return "ReservaEventMessage{" +
                "reservaId=" + reservaId +
                ", eventType='" + eventType + '\'' +
                '}';
    }

    public static class ReservaEventMessageBuilder {
        private Long reservaId;
        private String eventType;

        public ReservaEventMessageBuilder reservaId(Long reservaId) {
            this.reservaId = reservaId;
            return this;
        }

        public ReservaEventMessageBuilder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public ReservaEventMessage build() {
            return new ReservaEventMessage(reservaId, eventType);
        }
    }
}
