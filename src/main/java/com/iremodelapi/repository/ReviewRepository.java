package com.iremodelapi.repository;

import com.iremodelapi.domain.Review;
import com.iremodelapi.domain.Contractor;
import com.iremodelapi.domain.Homeowner;
import com.iremodelapi.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>
{
    List<Review> findByContractor(Contractor contractor);

    List<Review> findByHomeowner(Homeowner homeowner);

    List<Review> findByJob(Job job);

    // Calculate average rating for a contractor
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.contractor = :contractor")
    Double calculateAverageRatingForContractor(Contractor contractor);
}
