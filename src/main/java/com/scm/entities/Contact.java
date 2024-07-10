package com.scm.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contact {
	
	@Id
	private String id;
	
	private String name;
	
	private String email;
	
	private String phoneNumber;
	
	private String address;
	
	private String picture;
	
	@Column(length=1000)
	private String description;
	
	private boolean favorite = false;
	
	private String websiteLink;
	
	private String linkedinLink;
	
	private String publicId;
	
	@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<SocialLink> socialLinks = new ArrayList<>();
	
	@ManyToOne
	@JsonIgnore
	private User user;
}
