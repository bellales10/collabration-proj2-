app.factory('JobService',function($http) {
	var jobService={};
	var BASE_URL="http://localhost:8088/Backend"
	jobService.saveJob=function(job) 
	{	
		return $http.post(BASE_URL + "/postJob",job);
	}
	
	jobService.getAllJobs=function()
	{
		return $http.get(BASE_URL + "/getAllJobs");
	}
	
	 return jobService;
})