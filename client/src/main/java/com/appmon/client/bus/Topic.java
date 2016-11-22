package com.appmon.client.bus;

/**
 * Topics available for subscribers.
 */
public enum Topic {
    /**
     * Anything published to this topic will be sent to cloud.
     */
    WRITE_TO_CLOUD,
    /**
     * Any path published to this topic will be deleted.
     */
    DELETE_FROM_CLOUD,
    /**
     * Any pin updates are published to this topic.
     */
    PIN_UPDATE,
    /**
     * Any state updates for blocked applications are published to this topic.
     */
    APP_STATE_UPDATE,
    /**
     * Any messages related to blocking an app are published to this topic.
     */
    BLOCK_APP,
    /**
     * Any messages related to unblocking an app are published to this topic.
     */
    UNBLOCK_APP

}
