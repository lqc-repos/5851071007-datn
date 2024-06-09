import clientPromise from "@/lib/mongodb";
import { NextApiRequest, NextApiResponse } from "next";

export default async function findTopic(req: NextApiRequest, res: NextApiResponse) {
    try {
        const { topics, page = 1 } = req.query
        const client = await clientPromise;
        const db = client.db('thesis');
        const posts = await db
            .collection<any>('article')
            .find({ topics: [topics]})
            .limit(10)
            .skip(10 * (parseInt(page as string) - 1))
            .toArray();

        res.status(200).json(posts);
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Internal Server Error' });
    }
}