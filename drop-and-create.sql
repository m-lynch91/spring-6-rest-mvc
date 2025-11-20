
    create table beer (
        beer_style smallint not null check (beer_style between 0 and 9),
        price numeric(38,2) not null,
        quantity_on_hand integer,
        version integer,
        created_date timestamp(6),
        update_date timestamp(6),
        id varchar(36) not null,
        beer_name varchar(50) not null,
        upc varchar(255) not null,
        primary key (id)
    );

    create table customer (
        version integer,
        created_date timestamp(6),
        modified_date timestamp(6),
        id varchar(36) not null,
        name varchar(255),
        primary key (id)
    );

    create table beer (
        beer_style smallint not null check (beer_style between 0 and 9),
        price numeric(38,2) not null,
        quantity_on_hand integer,
        version integer,
        created_date timestamp(6),
        update_date timestamp(6),
        id varchar(36) not null,
        beer_name varchar(50) not null,
        upc varchar(255) not null,
        primary key (id)
    );

    create table customer (
        version integer,
        created_date timestamp(6),
        modified_date timestamp(6),
        id varchar(36) not null,
        name varchar(255),
        primary key (id)
    );
