create database imeplanMovil;
use imeplanMovil;

create table Categoria(
	id int auto_increment,
    categoria varchar(100),
    constraint pk_categoria primary key (id)
);

create table SubCategoria(
	id int auto_increment,
    categoria int,
    subcategoria varchar (100),
    primary key pk_subcategoria (id),
    constraint fk_categoria_sub foreign key (categoria) references Categoria(id)
);

create table Reporte(
	id int auto_increment,
    subcategoria int,
    latitud varchar(20),
    longitud varchar(20),
    foto varchar(150),
    fecha date,
    estado boolean,
    primary key pk_reporte (id),
    constraint fk_subcategoria_rep foreign key (subcategoria) references SubCategoria(id)
);