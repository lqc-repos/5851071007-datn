/* eslint-disable object-curly-newline */
/* eslint-disable no-shadow */
import { TableHandle } from '@/components/TableProvider';
import { Dispatch, RefObject, SetStateAction } from 'react';

export const totalAdminStatus = (admin_status_count: any) => {
    let count = 0;
    // eslint-disable-next-line no-restricted-syntax, guard-for-in
    for (const status in admin_status_count) {
        count += admin_status_count[status];
    }
    return count;
};

export const handleGetListUser = async (
    queries: any,
    page: number,
    limit: number,
    memberId: string,
    tableRef: RefObject<TableHandle>,
) => {
    tableRef?.current?.setLoading(true);
    const resp: any = await fetch('http://localhost:8080/user/list', {
        method: "POST",
        mode: "cors",
        credentials: "same-origin",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ page, size: limit, email: "", memberId, keyword: queries }),
    }).then((result) => result.json()).catch((e) => console.log(e));

    if (resp?.statusCode !== 200) return;
    if (resp?.statusCode === 200) {
        const { total, members, page, size } = resp.data;
        tableRef?.current?.dispatch({
            limit: size,
            page,
            total,
            data: members,
        });
    }
    tableRef?.current?.setLoading(false);
};

export function clearList(tableRef: RefObject<TableHandle>) {
    tableRef?.current?.dispatch({
        limit: 10,
        page: 1,
        total: 0,
        data: [],
    });
}
