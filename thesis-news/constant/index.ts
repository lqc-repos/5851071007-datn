import { CircleUser, DashBoard, SwalErrorIcon, SwalSuccessIcon, SwalWarningIcon } from "@/public/admin";
import { MenuItemProps } from "@/types/newpost";
import moment from "moment";

export const STYLE_LOGIN = {
    position: "absolute" as "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: 500,
    bgcolor: "background.paper",
    boxShadow: 24,
    p: 4,
}

export const HeightDefault = '46px';

export const PATH = {
    DASHBOARD: '/bao-cao-thong-ke',

    LOGIN: '/dang-nhap',
    My_ACCOUNT: '/tai-khoan',
    FORGOT_PASSWORD: '/quen-mat-khau',

    DON_HANG: '/don-hang',
    CHI_TIET_DON_HANG: '/don-hang/[id]',
};
export const Roles = {
    ADMIN_DASHBOARD: 'ADMIN_DASHBOARD', // Báo cáo thống kê',
    ADMIN_ORDER_LIST: 'ADMIN_ORDER_LIST', // 'Đơn hàng',
    ADMIN_PRESCRIPTION_LIST: 'ADMIN_PRESCRIPTION_LIST', // 'Đơn tư vấn',
    ADMIN_DIRECT_PRESCRIPTION_LIST: 'ADMIN_DIRECT_PRESCRIPTION_LIST', // 'Đơn tư vấn trực tiếp',
    ADMIN_ORDER_REMINDER: 'ADMIN_ORDER_REMINDER', // Nhắc nhở đơn hàng,
    ADMIN_CART_MANAGEMENT: 'ADMIN_CART_MANAGEMENT', // 'Quản lý giỏ hàng',
    ADMIN_PRICE_CONFIGURATION: 'ADMIN_PRICE_CONFIGURATION', // 'Điều chỉnh giá bán',
    ADMIN_PRODUCT_MANAGEMENT: 'ADMIN_PRODUCT_MANAGEMENT', // 'Quản lý sản phẩm',
    ADMIN_REQUEST_PURCHASE_ORDER: 'ADMIN_REQUEST_PURCHASE_ORDER', // 'Yêu vầu  mua hàng',
    ADMIN_VOUCHER_MANAGEMENT: 'ADMIN_VOUCHER_MANAGEMENT', // 'Quản lý voucher',
    ADMIN_GIFT_LIST: 'ADMIN_GIFT_LIST', // Danh sách quà tặng',
    ADMIN_PROMOTION_CATEGORY: 'ADMIN_PROMOTION_CATEGORY', // 'Danh mục khuyến mãi',
    ADMIN_BANNER_LIST: 'ADMIN_BANNER_LIST',
    ADMIN_NOTI_LIST: 'ADMIN_NOTI_LIST', // 'Đẩy thông báo',
    ADMIN_CUSTOMER_ACCOUNT_MANAGEMENT: 'ADMIN_CUSTOMER_ACCOUNT_MANAGEMENT', // 'Tài khoản khách hàng',
    ADMIN_MY_ACCOUNT: 'ADMIN_MY_ACCOUNT', // 'Tài khoản của tôi',
};
export const MenuTypes = { ACCORDION: 'ACCORDION', ROUTER: 'ROUTER' };

export const Menus: MenuItemProps[] = [
    {
        path: PATH.DASHBOARD,
        Icon: DashBoard,
        label: 'Báo cáo thống kê',
        type: MenuTypes.ROUTER,
        role: Roles.ADMIN_DASHBOARD,
        childrens: [],
    },
    {
        path: PATH.My_ACCOUNT,
        Icon: CircleUser,
        label: 'Quản lý tài khoản',
        type: MenuTypes.ROUTER,
        role: Roles.ADMIN_MY_ACCOUNT,
        childrens: [],
    },
];

export const rangeDateByDays = (toDate: string, fromDate: string, format?: string) => {
    const result: string[] = [];
    if (!moment(toDate).isValid() || !moment(fromDate).isValid()) {
        return result;
    }
    if (moment(toDate) > moment(fromDate)) {
        return result;
    }
    if (moment(toDate) === moment(fromDate)) {
        result.push(toDate);
    } else {
        const range = Math.abs(moment(toDate).diff(moment(fromDate), 'days'));
        // eslint-disable-next-line no-plusplus
        for (let i = 0; i <= range; i++) {
            result.push(moment(toDate).add(i, 'days').format(format));
        }
    }
    return result;
};

export const mappingType = {
    success: SwalSuccessIcon,
    warning: SwalWarningIcon,
    error: SwalErrorIcon,
};

export const ROLE: any = [
    {
        id: "admin_my_account",
        code: "ADMIN_MY_ACCOUNT",
        endpoints: [],
        name: ""
    },
    {
        id: "919ee30f-886d-4bad-8ce4-d52639acfe86",
        name: "CIRCA_ADMIN - Quản lý giỏ hàng",
        code: "ADMIN_CART_MANAGEMENT",
        is_enabled: true,
        endpoints: [],
        created_at: "2023-10-06T06:34:23.995964Z",
        updated_at: "2023-11-27T03:43:45.689545Z"
    },
    {
        id: "076f23d1-6950-4a24-8783-89efa9a88fbc",
        name: "CIRCA_ADMIN - Danh sách thông báo",
        code: "ADMIN_NOTI_LIST",
        is_enabled: true,
        endpoints: [],
        created_at: "2023-10-06T06:34:23.995964Z",
        updated_at: "2023-11-27T03:43:50.712842Z"
    },
    {
        id: "42632578-8120-4fa0-9890-adc1f85c6369",
        name: "CIRCA_ADMIN - Nhắc nhở đơn hàng",
        code: "ADMIN_ORDER_REMINDER",
        is_enabled: true,
        endpoints: [],
        created_at: "2023-11-27T03:49:24.305057Z",
        updated_at: "2023-11-27T03:50:34.556376Z"
    },
    {
        id: "1834bde4-b2b4-4566-a4cd-43f6e109a4c6",
        name: "CIRCA_ADMIN - Quản lý voucher",
        code: "ADMIN_VOUCHER_MANAGEMENT",
        is_enabled: true,
        endpoints: [],
        created_at: "2023-11-14T03:04:55.694173Z",
        updated_at: "2023-11-27T03:56:46.607492Z"
    },
    {
        id: "99b2884c-f24a-4d07-b2d6-f4cfa1c44030",
        name: "Circa Admin - Quản lý chuyển khoản",
        code: "ADMIN_TRANSACTION_MANAGEMENT",
        is_enabled: true,
        endpoints: [],
        created_at: "2024-01-04T02:40:27.484878Z",
        updated_at: "2024-01-04T02:40:27.443351Z"
    },
    {
        id: "f19c67a4-97ff-490d-8498-8dd44f9744e0",
        name: "CIRCA_ADMIN - Quản lý deals",
        code: "ADMIN_DEAL_MANAGEMENT",
        is_enabled: true,
        endpoints: [],
        created_at: "2024-01-04T03:21:45.121614Z",
        updated_at: "2024-01-04T03:21:45.107800Z"
    },
    {
        id: "c48ae379-189c-4b68-8448-ded77bc20f96",
        name: "CIRCA_ADMIN - Quản lý từ khóa",
        code: "ADMIN_KEYWORD_MANAGEMENT",
        is_enabled: true,
        endpoints: [],
        created_at: "2024-03-26T02:41:07.457633Z",
        updated_at: "2024-03-26T02:41:07.416393Z"
    },
    {
        id: "ae17e4b1-16d0-40a2-b70b-f47bb0110672",
        name: "CIRCA_ADMIN - Danh sách quà tặng",
        code: "ADMIN_GIFT_LIST",
        is_enabled: true,
        endpoints: [],
        created_at: "2023-11-27T03:37:40.268105Z",
        updated_at: "2023-11-27T03:42:01.869909Z"
    },
    {
        id: "19ae4582-65ca-4b8a-b205-283a743cf771",
        name: "CIRCA_ADMIN - Danh sách đơn tư vấn trực tiếp",
        code: "ADMIN_DIRECT_PRESCRIPTION_LIST",
        is_enabled: true,
        endpoints: [],
        created_at: "2023-10-11T09:15:13.875849Z",
        updated_at: "2023-11-27T03:42:09.187137Z"
    },
    {
        id: "d77e42e4-a488-4e02-991c-84778cf1961b",
        name: "CIRCA_ADMIN - Điều chỉnh giá bán",
        code: "ADMIN_PRICE_CONFIGURATION",
        is_enabled: true,
        endpoints: [],
        created_at: "2023-10-06T06:34:23.995964Z",
        updated_at: "2023-11-27T03:42:16.579407Z"
    },
    {
        id: "a94f1a4c-3bec-4d95-9e89-46ee26903f61",
        name: "CIRCA_ADMIN - Quản lý sản phẩm",
        code: "ADMIN_PRODUCT_MANAGEMENT",
        is_enabled: true,
        endpoints: [],
        created_at: "2023-10-06T06:34:23.995964Z",
        updated_at: "2023-11-27T03:42:27.655101Z"
    },
    {
        id: "663d95fb-802c-44f8-be24-1dc90950e690",
        name: "CIRCA_ADMIN - Yêu cầu mua hàng",
        code: "ADMIN_REQUEST_PURCHASE_ORDER",
        is_enabled: true,
        endpoints: [],
        created_at: "2023-10-06T06:34:23.995964Z",
        updated_at: "2023-11-27T03:42:35.615110Z"
    },
    {
        id: "2601d97b-9bfa-446c-a26b-9b4eaf5eff45",
        name: "CIRCA_ADMIN - Danh mục khuyến mãi",
        code: "ADMIN_PROMOTION_CATEGORY",
        is_enabled: true,
        endpoints: [],
        created_at: "2023-10-06T06:34:23.995964Z",
        updated_at: "2023-11-27T03:42:40.841659Z"
    },
    {
        id: "2a915e59-acc4-4999-aa5d-d6fc1d6565ab",
        name: "CIRCA_ADMIN - Danh sách banner",
        code: "ADMIN_BANNER_LIST",
        is_enabled: true,
        endpoints: [],
        created_at: "2023-10-06T06:34:23.995964Z",
        updated_at: "2023-11-27T03:42:49.712517Z"
    },
    {
        id: "f131e34c-8edb-4578-ace7-caa9d0da7a64",
        name: "CIRCA_ADMIN - Tài khoản khách hàng",
        code: "ADMIN_CUSTOMER_ACCOUNT_MANAGEMENT",
        is_enabled: true,
        endpoints: [],
        created_at: "2023-10-06T06:34:23.995964Z",
        updated_at: "2023-11-27T03:43:20.452073Z"
    },
    {
        id: "1f36dbd1-9ac1-4acb-a959-fa74a4516caa",
        name: "CIRCA_ADMIN - Báo cáo thống kê",
        code: "ADMIN_DASHBOARD",
        is_enabled: true,
        endpoints: [],
        created_at: "2023-10-06T06:34:23.995964Z",
        updated_at: "2023-11-27T03:43:27.914616Z"
    },
    {
        id: "25b5b8e1-6646-4a61-8ca4-28b653e78168",
        name: "CIRCA_ADMIN - Đơn tư vấn",
        code: "ADMIN_PRESCRIPTION_LIST",
        is_enabled: true,
        endpoints: [],
        created_at: "2023-10-06T06:34:23.995964Z",
        updated_at: "2023-11-27T03:43:38.365234Z"
    },
    {
        id: "64781e4b-82b9-4a88-a5b5-11a908268479",
        name: "Quản lý Phiếu xuất kho",
        code: "ADMIN_DELIVERY_NOTE",
        is_enabled: true,
        endpoints: [],
        created_at: "2024-05-14T08:18:48.047914Z",
        updated_at: "2024-05-14T08:18:48.046066Z"
    },
    {
        id: "baf421a2-8375-4294-bb4a-5506e4fc5950",
        name: "CIRCA_ADMIN - Quản lý trang chủ",
        code: "ADMIN_HOMEPAGE_MANAGEMENT",
        is_enabled: true,
        endpoints: [],
        created_at: "2024-04-24T07:11:14.242359Z",
        updated_at: "2024-04-24T07:11:33.526800Z"
    },
    {
        id: "86b3b4b6-78b6-4902-a9c1-803fc161cc10",
        name: "CIRCA_ADMIN - Quản lý tài khoản",
        code: "ADMIN_ACCOUNT_MANAGEMENT",
        is_enabled: true,
        endpoints: [],
        created_at: "2024-05-03T07:19:20.597807Z",
        updated_at: "2024-05-03T07:23:39.516381Z"
    },
    {
        id: "455a064d-4bde-4228-99bc-c8d3207ddeac",
        name: "CIRCA_ADMIN - Người dùng POS",
        code: "ADMIN_USER_POS",
        is_enabled: true,
        endpoints: [],
        created_at: "2024-05-08T06:37:05.826250Z",
        updated_at: "2024-05-08T06:37:05.823283Z"
    },
    {
        id: "ae2204d5-76e9-4c4c-9768-2a8ba49e5e40",
        name: "CIRCA_ADMIN - Quản lý đối soát",
        code: "ADMIN_RECONCILIATION_MANAGEMENT",
        is_enabled: true,
        endpoints: [],
        created_at: "2024-05-02T03:29:51.593622Z",
        updated_at: "2024-05-09T04:22:03.356831Z"
    },
    {
        id: "00853c5a-a7c7-4c82-86da-a065b55d4fc0",
        name: "CIRCA_ADMIN - Quản trị viên",
        code: "ADMIN_ADMINISTRATORS",
        is_enabled: true,
        endpoints: [],
        created_at: "2024-05-10T10:00:31.459835Z",
        updated_at: "2024-05-10T10:00:53.631086Z"
    },
    {
        id: "1935f5cf-036a-4d2f-901e-652b2ea11188",
        name: "CIRCA_ADMIN - Vai trò",
        code: "ADMIN_ROLES",
        is_enabled: true,
        endpoints: [],
        created_at: "2024-05-10T10:14:52.401416Z",
        updated_at: "2024-05-10T10:14:52.398363Z"
    },
    {
        id: "42e8ae07-5005-440f-a137-f50610872cc5",
        name: "CIRCA_ADMIN - Nhóm quyền",
        code: "ADMIN_PERMISSION",
        is_enabled: true,
        endpoints: [],
        created_at: "2024-05-10T10:22:09.111545Z",
        updated_at: "2024-05-10T10:22:09.109351Z"
    },
    {
        id: "33686b7c-02ab-4fd9-9691-a664bb0f6d68",
        name: "CIRCA_ADMIN - Đơn hàng",
        code: "ADMIN_ORDER_LIST",
        is_enabled: true,
        endpoints: [],
        created_at: "2023-10-06T06:34:23.995964Z",
        updated_at: "2024-05-15T09:24:22.140501Z"
    },
    {
        id: "05bd9518-20c4-4c33-bcda-9653881b5159",
        name: "Quản lý lệnh điều động",
        code: "ADMIN_MOBILIZATION",
        is_enabled: true,
        endpoints: [],
        created_at: "2024-05-15T09:36:52.956981Z",
        updated_at: "2024-05-16T06:15:33.484971Z"
    },
    {
        id: "0fd43d97-ac15-4e08-9fce-86b63e7a2195",
        name: "CIRCA_ADMIN  - Quản lý hàng ký gửi",
        code: "ADMIN_CONSIGNMENT_ORDER_MANAGEMENT",
        is_enabled: true,
        endpoints: [],
        created_at: "2024-05-14T06:16:51.669416Z",
        updated_at: "2024-05-23T07:13:04.818412Z"
    }
]
