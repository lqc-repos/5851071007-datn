import { create } from 'zustand'

type State = {
    user: string | null
}

type Action = {
    addUser: (firstName: State['user']) => void
}

// Create your store, which includes both state and (optionally) actions
export const usePersonStore = create<State & Action>((set) => ({
    user: null,
    addUser: (firstName) => set(() => ({ user: firstName })),
}))
