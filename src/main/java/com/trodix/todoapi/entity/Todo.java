package com.trodix.todoapi.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.trodix.todoapi.core.entity.User;
import com.trodix.todoapi.core.interfaces.Ownable;

import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

@Entity
@Getter @Setter	
public class Todo implements Ownable {
	
	@Setter(AccessLevel.PROTECTED)
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	private String title;

	private Boolean done = false;

	@ManyToOne
	private User user;

}