package com.wjm.bootbook.entity.common;

/**
 * @author stephen wang
 */

public class RabbitMqConstant {
    public static final String ES_EXCHANGE_NAME = "es";
    public static final String REDIS_EXCHANGE_NAME = "redis";

    public static final String MINIO_EXCHANGE_NAME = "minio";

    public static final String ES_ADD_QUEUE_NAME = "es.add";
    public static final String ES_DELETE_QUEUE_NAME = "es.delete";
    public static final String ES_UPDATE_QUEUE_NAME = "es.update";

    public static final String REDIS_ADD_QUEUE_NAME = "redis.add";
    public static final String REDIS_DELETE_QUEUE_NAME = "redis.delete";
    public static final String REDIS_UPDATE_QUEUE_NAME = "redis.update";
    public static final String REDIS_EXPIRE_QUEUE_NAME = "redis.expire";

    public static final String MINIO_UPLOAD_QUEUE_NAME = "minio.upload";
    public static final String KEYWORD_ADD = "add";
    public static final String KEYWORD_DELETE = "delete";
    public static final String KEYWORD_UPDATE = "update";

    public static final String KEYWORD_EXPIRE = "expire";
    public static final String KEYWORD_UPLOAD = "upload";
}
