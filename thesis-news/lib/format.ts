import { mappingType } from '@/constant';
import { toast } from 'react-toastify';

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

const notifyContent = {
    ADD_SUCCESS: 'Thêm thành công',
    UPDATE_SUCCESS: 'Cập nhật thành công',
    DELETE_SUCCESS: 'Xóa thành công',
    IMPORT_SUCCESS: 'Import thành công',
    EXPORT_SUCCESS: 'Export thành công',
    CANCEL_SUCCESS: 'Hủy thành công',
    REFUND_SUCCESS: 'Hoàn tiền thành công',
    ADD_FAIL: 'Thêm thất bại',
    UPDATE_FAIL: 'Cập nhật thất bại',
    DELETE_FAIL: 'Xóa thất bại',
    CANCEL_FAIL: 'Hủy thất bại',
    SYSTEM_ERROR: 'Lỗi hệ thống',
    NOT_FOUND: 'Không tìm thấy',
    REFUND_FAIL: 'Hoàn tiền thất bại',
};

enum notifyType {
    SUCCESS = 'success',
    ERROR = 'error',
    WARNING = 'warning',
}
const autoClose = 5000;

const adminNotify = (message: string, type: 'success' | 'warning' | 'error', option: object = {}) => {
    switch (type) {
        case 'success': toast.success(message, {
            icon: mappingType[type],
            autoClose,
            style: {
                backgroundColor: 'white',
                color: 'black',
            },
            ...option,
        }); break;
        case 'error': toast.error(message, {
            icon: mappingType[type],
            autoClose,
            style: {
                backgroundColor: 'white',
                color: 'black',
            },
            ...option,
        }); break;
        case 'warning': toast.warning(message, {
            icon: mappingType[type],
            autoClose,
            style: {
                backgroundColor: 'white',
                color: 'black',
            },
            ...option,
        }); break;
        default: break;
    }
};

const notify = (message: string, type: 'success' | 'warning' | 'error', option: object = {}) => {
    switch (type) {
        case 'success': toast.success(message, {
            icon: mappingType[type],
            autoClose,
            ...option,
        }); break;
        case 'error': toast.error(message, {
            icon: mappingType[type],
            autoClose,
            ...option,
        }); break;
        case 'warning': toast.warning(message, {
            icon: mappingType[type],
            autoClose,
            ...option,
        }); break;
        default: break;
    }
};
export {
    adminNotify,
    notify,
    notifyContent,
    notifyType,
};