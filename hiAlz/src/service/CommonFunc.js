function debounce(fn, delay) {
    let timer = null;
    return function() {
        if (timer !== null) {
            clearTimeout(timer);
        }
        timer = setTimeout(() => {
            fn.apply(this, arguments);
        }, delay);
    }
}

function throttle(fn, delay) {
    let lastExeTime = Date.now();
    return function() {
        let curTime = Date.now();
        if (curTime - lastExeTime >= delay) {
            lastExeTime = curTime;
            fn.apply(this, arguments);
        }
    }
}

export default {
    debounce,
    throttle,
}