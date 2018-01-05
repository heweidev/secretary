function createPromise(item) {
    return new Promise(function(resolve, reject){
        resolve(item);
    });
}

Promise.all([1, 2, 3].map(function(item){
    return createPromise(item);
})).then(function(values){
    console.log(values);
}).catch(function(err){
    console.log(err);
});