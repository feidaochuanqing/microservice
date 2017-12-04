--产品表，产品类别表，订单表。字段自己定义，但三张表是有关联的。

--要求完成产品、产品类别、订单的增删改查功能。

--产品表中存储着产品类别编码，在产品的列表页面要显示出产品类别名称。（使用JPA的连表查询）

--在订单的列表页面要显示出每个订单中产品的数量列。

--产品表
drop table masterdata_product;
create table masterdata_product(
  id varchar(50) primary key,
  product_code varchar(50),
  product_name varchar2(50),
  fk_product_category varchar(50),
  create_time date,
  create_user varchar2(50),
  last_modify_time date,
  last_modify_user varchar2(50),
  is_deleted number(1),
  delete_time date,
  delete_user varchar2(50)
);

--产品类别表
drop table masterdata_product_category;
create table masterdata_product_category(
  id varchar(50)  primary key,
  category_code varchar(50),
  category_name varchar2(50),
  create_time date,
  create_user varchar2(50),
  last_modify_time date,
  last_modify_user varchar2(50),
  is_deleted number(1),
  delete_time date,
  delete_user varchar2(50)
);

--订单表
drop table trade_order;
create table trade_order(
  id varchar(50)  primary key,
  fk_product varchar(50) not null,
  count int,
  address varchar2(200),
  create_time date,
  create_user varchar2(50),
  last_modify_time date,
  last_modify_user varchar2(50),
  is_deleted number(1),
  delete_time date,
  delete_user varchar2(50)
);

