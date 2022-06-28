db.people.mapReduce(
    function () {
        this.credit.forEach(credit => {
            emit(credit.currency, credit.balance)
        });
    },
    function (key, values) {
        const sum = values.reduce((accumulator, a) => {
            return accumulator + parseFloat(a);
        }, 0);
        return {
            "sum_balance": sum,
            "avg_balance": sum / values.length
        }
    },
    {
        out: "balance",
        query: {
            nationality: "Poland",
            sex: "Female"
        }
    }
)
printjson(db.balance.find().toArray())