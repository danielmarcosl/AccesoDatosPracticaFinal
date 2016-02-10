create table if not exists habitacion(
cod_hab smallint(3) primary key,
planta smallint(2),
num_cama smallint(3) unique,
doctorasignado varchar(20)
);

create table if not exists paciente(
cod_pac integer primary key,
dni varchar(10) unique,
nombre varchar(20),
edad integer,
sexo char(1),
alergias varchar(20),
cod_hab smallint(3),
constraint fk_paciente_cod_hab foreign key (cod_hab) references habitacion(cod_hab)
);

insert into habitacion values
(101,1,1,'El barbas'),
(102,1,2,'Peluquero'),
(201,2,3,'El tirillas'),
(202,2,4,'Cigala'),
(301,3,5,'Pasarlo bien'),
(302,3,6,'Aprovecharlo');