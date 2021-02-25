create or replace database tododb;

use tododb;

create table todo (
    id int not null auto_increment,
    name varchar(255) not null,
    description text null,
    done tinyint(1) default false,
    primary key(id)
) character set utf8 collate utf8_general_ci;