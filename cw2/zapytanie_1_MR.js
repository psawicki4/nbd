db.people.mapReduce(
    function () {
        const value = {weight: parseFloat(this.weight), height: parseFloat(this.height), count: 1};
        emit(this.sex, value);
    },
    function (key, values) {
        const redVal = {weight: 0, height: 0, count: 0};

        for (let id = 0; id < values.length; id++) {
            redVal.count += values[id].count;
            redVal.weight += values[id].weight;
            redVal.height += values[id].height;
        }
        return {
            "avg_height": redVal.height / redVal.count,
            "avg_weight": redVal.weight / redVal.count
        }
    },
    {
        out: "heightWeight"
    }
)
printjson(db.heightWeight.find().toArray())