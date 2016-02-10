create table if not exists habitacion(
cod_hab integer primary key autoincrement,
planta smallint(2),
num_cama smallint(3),
doctorasignado varchar(20),
ocupado char(1)
);

create table if not exists paciente(
cod_pac integer primary key autoincrement,
dni varchar(10) unique,
nombre varchar(20),
edad integer,
sexo char(1),
alergias varchar(20),
cod_hab smallint(3),
constraint fk_paciente_cod_hab foreign key (cod_hab) references habitacion(cod_hab)
);

insert into habitacion values
(null,1,101,'Papo','N'),
(null,1,102,'Golondrio','S'),
(null,1,103,'Papo','N'),
(null,2,201,'Papo','N'),
(null,2,202,'Golondrio','S'),
(null,2,203,'Berer','S'),
(null,3,301,'Berer','N'),
(null,3,302,'Buenaesa','S'),
(null,3,303,'Papo','N'),
(null,4,401,'Berer','N'),
(null,4,402,'Golondrio','N'),
(null,4,403,'Golondrio','N');

insert into paciente values
(null,'1234A','Martinez',25,'H','Muchas',2),
(null,'5612C','Birreta',24,'M','Pocas',5),
(null,'5647E','Siberaso',19,'M','Unas cuantas',6),
(null,'7598J','sqlito',34,'H','Tres',8);
