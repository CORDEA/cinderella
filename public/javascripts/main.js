$('.timepicker').pickatime({
    default: 'now',
    fromnow: 0,
    twelvehour: false,
    donetext: 'OK',
    cleartext: 'Clear',
    canceltext: 'Cancel',
    autoclose: false,
    ampmclickable: true,
    aftershow: function(){}
});

function updateTime(current) {
    const elem = document.getElementById("current");
    elem.innerText = current;
}

async function fetchCurrentTime() {
    try {
        const response = await fetch("/current");
        return await response.json();
    } catch (e) {
    }
}

async function wait(ms) {
    return new Promise(r => setTimeout(r, ms))
}

async function startRefreshCurrentTime() {
    const response = await fetchCurrentTime()
    console.log(response);
    if (response.is_stopped) {
        return;
    }
    updateTime(response.current)
    await wait(1000);
    await startRefreshCurrentTime();
}

startRefreshCurrentTime();
