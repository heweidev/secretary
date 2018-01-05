
function err(code , msg) {
    return {
        err: err,
        msg: msg
    }
}

function param_err(msg) {
    return {
        err: 1001,
        msg: msg
    }
}

module.export = {
   err: err,
   p_err: param_err 
};