package com.zenflix.ott.entity;

import java.util.List;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "videos")
public class Video extends Auditable{
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @NotBlank(message = "Title cannot be blank")
 @Column(nullable = false)
 private String title;

 @Column(nullable = false)
 private String description;
 
 @URL(message = "Invalid URL format")
 @Column(length = 255, nullable = true) // Allow nullable URLs
 private String url;

 @Column(nullable = false)
 private Boolean deleted = false;

 @ManyToOne
 @JoinColumn(name = "genre_id")
 private Genre genre;

 @OneToMany(mappedBy = "video")
 private List<Watchlist> watchlist;
 
 @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
 private Boolean isPublicTrailer = false;

}