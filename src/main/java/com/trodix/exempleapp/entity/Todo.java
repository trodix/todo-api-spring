package com.trodix.exempleapp.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

@Entity
@Getter @Setter	
public class Todo {
	
	@Setter(AccessLevel.PROTECTED)
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	private String title;

	@ManyToOne
	private User user;

}