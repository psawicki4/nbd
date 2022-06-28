db.people.mapReduce(
    function () {
        const bmi = parseFloat(this.weight) / Math.pow((parseFloat(this.height) / 100), 2);
        emit(this.nationality, {bmi: bmi, min_bmi: bmi, max_bmi: bmi, count: 1});
    },
    function (key, values) {
        let redVal = {count: 0, bmi: 0, avg_bmi: 0, min_bmi: values[0].min_bmi, max_bmi: values[0].max_bmi};
        values.forEach(val => {
            redVal.count += val.count;
            redVal.bmi += val.bmi;
            redVal.min_bmi = Math.min(redVal.min_bmi, val.min_bmi);
            redVal.max_bmi = Math.max(redVal.max_bmi, val.max_bmi);
        });
        return {
            avg_bmi: redVal.bmi / redVal.count,
            min_bmi: redVal.min_bmi,
            max_bmi: redVal.max_bmi
        };
    },
    {
        out: "bmi"
    }
)
printjson(db.bmi.find().toArray())