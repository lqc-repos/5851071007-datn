export const formatDate = (time: any) => {

    const date = new Date(time).toLocaleDateString('vi-VN');;
    return date;
}

export const formatDateHourMinute = (time: any) => {
    const date = new Date(time)
    let localeSpecificTime = date.toLocaleTimeString();
    return localeSpecificTime.replace(/:\d+ /, ' ');
}

export const capitalizeFirstLetter = (data: string) => {
    if (typeof data !== 'string' || data.length === 0) {
        return '';
    }
    return data.charAt(0).toUpperCase() + data.slice(1).toLowerCase();
}

export const getRandomInt = (max: number) => {
    return Math.floor(Math.random() * max);
}