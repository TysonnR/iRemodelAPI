package com.iremodelapi.web.controller;

import com.iremodelapi.service.MatchService;
import com.iremodelapi.web.dto.ContractorMatchDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class MatchController
{
    private final MatchService matchService;

    public MatchController(MatchService matchService)
    {
        this.matchService = matchService;
    }

    @GetMapping("/{jobId}/matches")
    public ResponseEntity<List<ContractorMatchDTO>> getMatchesForJob(@PathVariable Long jobId)
    {
        try
        {
            List<ContractorMatchDTO> matches = matchService.findMatches(jobId);
            return ResponseEntity.ok(matches);
        }
        catch (IllegalArgumentException e)
        {
            return ResponseEntity.notFound().build(); //404
        }

    }

}
