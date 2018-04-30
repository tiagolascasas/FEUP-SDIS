pragma foreign_keys = off;

drop table if exists User;
drop table if exists Track;

create table User
(
	idUser integer primary key,
	password text not null
);

create table Track
(
	idTrack integer primary key,
	title text not null,
	content text not null,
	dateAdded date not null,
	idUser not null,
	constraint user_fkey foreign key (idUser) references User
);

