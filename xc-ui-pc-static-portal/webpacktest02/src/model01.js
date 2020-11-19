const add = function (x, y) {
    return x + y;
}

const add2 = function (x, y) {
    return x + y + 2;
}

// 导出model01
module.exports.add = add;
// module.exports.add = add2;
// module.exports.add = {add,add2};