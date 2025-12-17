DROP TABLE IF EXISTS beer_order_line CASCADE;
DROP TABLE IF EXISTS beer_order CASCADE;

CREATE TABLE beer_order (
    id                  varchar(36) NOT NULL,
    created_date        timestamp(6),
    customer_ref        varchar(255),
    last_modified_date  timestamp(6),
    version             bigint,
    customer_id         varchar(36),
    CONSTRAINT beer_order_pk PRIMARY KEY (id),
    CONSTRAINT beer_order_customer_fk
        FOREIGN KEY (customer_id) REFERENCES customer (id)
);

CREATE TABLE beer_order_line (
    id                  varchar(36) NOT NULL,
    beer_id             varchar(36),
    created_date        timestamp(6),
    last_modified_date  timestamp(6),
    order_quantity      int,
    quantity_allocated  int,
    version             bigint,
    beer_order_id       varchar(36),
    CONSTRAINT beer_order_line_pk PRIMARY KEY (id),
    CONSTRAINT beer_order_line_order_fk
        FOREIGN KEY (beer_order_id) REFERENCES beer_order (id),
    CONSTRAINT beer_order_line_beer_fk
        FOREIGN KEY (beer_id) REFERENCES beer (id)
);
