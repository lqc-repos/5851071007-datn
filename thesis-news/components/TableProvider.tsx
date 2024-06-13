/* eslint-disable react/display-name */
import {
  createContext,
  forwardRef,
  ReactNode,
  useContext,
  useImperativeHandle,
  useReducer,
} from "react";
import { useSearchParams } from "next/navigation";
import { ValueOf } from "@/types/newpost";

type AnyObject = {
  [x: string]: any;
}

export type TableState<T> = {
  data: T[];
  limit: number;
  page: number;
  total: number;
  loading?: boolean;
};

const initialState = {
  data: [],
  limit: 10,
  page: 1,
  total: 0,
  loading: false,
};

export type ReducerAction<T> = {
  type:
    | "setData"
    | "setLimit"
    | "setPage"
    | "setTotal"
    | "setState"
    | "setLoading";
  payload: Partial<TableState<T>>;
};

export function TableReducer<T>(
  state: TableState<T>,
  action: ReducerAction<T>
): TableState<T> {
  switch (action.type) {
    case "setData":
      return { ...state, data: action.payload.data ?? [] };
    case "setLimit":
      return { ...state, limit: action.payload.limit ?? 0 };
    case "setPage":
      return { ...state, page: action.payload.page ?? 0 };
    case "setTotal":
      return { ...state, page: action.payload.total ?? 0 };
    case "setState":
      return { ...state, ...action.payload };
    case "setLoading":
      return { ...state, loading: action.payload.loading ?? false };
    default:
      return state;
  }
}
type TableContextValue = TableState<any> & {
  dispatch?: (_reducerAction: ReducerAction<AnyObject>) => void;
};

export interface TableHandle {
  dispatch: (_data: TableState<AnyObject>) => void;
  setLoading: (_loading: boolean) => void;
  setLimit: (_limit: number) => void;
  getStates: (
    _key?: "" | keyof TableState<AnyObject>
  ) => ValueOf<AnyObject> | null;
}

export const TableContext = createContext<TableContextValue>(initialState);

export const UseTableContext = () => useContext(TableContext);

const TableProvider = forwardRef<
  TableHandle | undefined,
  { children: ReactNode }
>(({ children }, ref) => {
  const router = useSearchParams();
  const page = Number(router?.get('list_page'))
    ? Number(router?.get('list_page'))
    : 1;
  const [states, dispatch] = useReducer(TableReducer, {
    ...initialState,
    page,
  });
  useImperativeHandle(ref, () => ({
    dispatch: (data: Partial<TableState<AnyObject>>) =>
      dispatch({
        type: "setState",
        payload: data,
      }),
    setLoading: (loading: boolean) =>
      dispatch({
        type: "setLoading",
        payload: { loading },
      }),
    setLimit: (limit: number) =>
      dispatch({
        type: "setLimit",
        payload: { limit },
      }),
    getStates: (key: keyof TableState<AnyObject> | "" = "") => {
      if (!key) return { ...states };

      try {
        return { ...states }[key];
      } catch {
        return null;
      }
    },
  }));

  return (
    // eslint-disable-next-line react/jsx-no-constructed-context-values
    <TableContext.Provider value={{ ...states, dispatch }}>
      {children}
    </TableContext.Provider>
  );
});

export default TableProvider;
