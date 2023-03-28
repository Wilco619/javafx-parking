create table customer_details (
 id int(20) primary auto_increment,
 usrName varchar(20),
 reg_no varchar(20),
 slot_id varchar(20),
 time_in default (current_time),
 time_out default (current_time),
 paid bit,
 amount varchar(20)
 )

 create table payments (
 id int(20) primary auto_increment,
 payment_id varchar(20),
 slot_id varchar(20)
 )

 create table admin_login (
 id int(20) primary key auto_increment not null,
 admin_id varchar(20) not null,
 name varchar(20) not null,
 usrName varchar(20) not null,
 email varchar(20) not null,
 phone int(20) not null,
 pwd varchar(20) not null,
 pwdRepeat varchar(20) not null
 ) values ("001","11","11","11","11","11","11");

 create table added_slot (
 id int(20) primary key auto_increment not null,
 slot_Id varchar(20),
 slot_Location varchar(20)
 );

DELETE FROM dbo.table1
OUTPUT DELETED.* INTO @MyTable  --or temp table
WHERE id = 10

