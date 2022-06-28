db.people.mapReduce( 
    function() {
        emit(this.job, 1); }, 
    function(key, values) {
		return values.reduce((accumulator, a) => {
		  return accumulator + parseFloat(a);
		}, 0);
    },
    {
        out: "jobs",
    }
 )
 printjson(db.jobs.find({value: 1}).toArray())