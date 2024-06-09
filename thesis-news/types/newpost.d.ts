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
    childrens:{
        path: string
        label: string
        Icon?: FunctionComponent<SVGProps>
        show: boolean
        role?: string
        childrens?: MenuItemChildren[]
    }[]
}