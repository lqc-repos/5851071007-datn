interface IFormInputNewPost {
    title: string
    author: string
    description: string
}

export interface Column {
    id: "name" | "code" | "population" | "size" | "density";
    label: string;
    minWidth?: number;
    align?: "right";
    format?: (value: number) => string;
}

export interface Data {
    name: string;
    code: string;
    population: number;
    size: number;
    density: number;
}

export interface MenuItemProps {
    path: string
    label: string
    Icon: FunctionComponent<SVGProps>
    type: string
    // eslint-disable-next-line react/no-unused-prop-types
    role: string
    childrens: {
        path: string
        label: string
        Icon?: FunctionComponent<SVGProps>
        show: boolean
        role?: string
        childrens?: MenuItemChildren[]
    }[]
}

export interface EntityEndPiont {
    id: string,
    display_name: string,
    endpoint_path: string,
}

export interface EntityRoleAssign {
    id: string,
    code: string,
    name: string,
    is_enabled: boolean,
    endpoints: EntityEndPiont[],
}

export interface MenuItemChildren {
    path: string;
    label: string;
    Icon?: FunctionComponent<SVGProps>;
    show: boolean;
}

export interface SVGProps {
    className?: string;
    fill?: HexColor;
    width?: number | string;
    height?: number | string;
}

export type ValueOf<T> = T[keyof T]

export interface OrderColumnProps {
    onChangeFilterDataColumns?: (_value: FilterDataColumnProps) => void,
    filterDataColumns?: FilterDataColumnProps
}

interface OrderTableProps extends OrderColumnProps {
    onChangePage: (_page: number) => void;
    onChangeLimit: (_limit: number) => void;
}

interface CustomDataGridProps {
    onChangePage: (page: number) => void;
    onChangeLimit: (limit: number) => void;
    total: number;
    limit: number;
    page: number;
    showDensity?: boolean;
    calculateRowsPerPage?: number;
    headerHeight?: number;
    fixedHeight?: number;
}

interface UserChartProps {
    title?: string;
    total?: number;
    userType?: "activeUsers" | "newUsers";
    className?: string;
    setTotalUser?: (e: number) => void;
    setTotalRegiter?: (e: number) => void;
}
