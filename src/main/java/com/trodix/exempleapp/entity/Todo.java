package com.trodix.exempleapp.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

@Entity
@Getter @Setter	
public class Todo {
	
	@Setter(AccessLevel.PROTECTED)
	@Id @GeneratedValue
	private UUID id;
	
	private String title;

}