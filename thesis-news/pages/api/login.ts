import clientPromise from "@/lib/mongodb";
import { NextApiRequest, NextApiResponse } from "next";

export default async function loginUser(req: NextApiRequest, res: NextApiResponse) {
    try {
        const { email, password } = req.query
        // console.log("EMAIL", email, password)
        const client = await clientPromise;
        const db = client.db('thesis');
        const user = await db
            .collection<any>('account')
            .findOne({ email, password })

        if (!user) {
            res.status(404).json({ message: 'User not found', code: 400 });
            return;
        }

        res.status(200).json(user);
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Internal Server Error' });
    }
}