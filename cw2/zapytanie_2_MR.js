db.people.mapReduce(
    function () {
        this.credit.forEach(credit => {
            emit(credit.currency, credit.balance)
        });
    },
    function (key, values) {
        return values.reduce((accumulator, a) => {
            return accumulator + parseInt(a);
        }, 0);
    },
    {
        out: "balance",
    }
)
printjson(db.balance.find().toArray())