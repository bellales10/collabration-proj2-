package com.backend.mvc.dao;

import java.util.List;

import com.backend.mvc.model.Job;

public interface JobDao {
	
	void postJob(Job job);
	List<Job> getAllJobs();

}
