package com.trodix.exempleapp.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Todo {
	
	@Id @GeneratedValue
	UUID id;
	
	String title;


	public UUID getId() {
		return id;
    }
    
	public String getTitle() {
		return title;
    }
    
	public Todo setTitle(String title) {
		this.title = title;
		return this;
	}
}