create table t_order (
    tracking_id VARCHAR(32) not null,
    status varchar(50) not null,
    CONSTRAINT pk_order PRIMARY KEY (tracking_id)
);

