import clientPromise from "@/lib/mongodb";
import { NextApiRequest, NextApiResponse } from "next";

export default async function filterTopic(req: NextApiRequest, res: NextApiResponse) {
    try {
        const { topics, startDate, endDate } = req.query
        if (!startDate || !endDate) {
            res.status(404).json({ message: 'Bad Request', code: 400 });
            return;
        }
        const client = await clientPromise;
        const db = client.db('thesis');
        const posts = await db
            .collection<any>('article')
            .find({ topics: [topics], publicationDate: {$gte: parseInt(startDate as string), $lte: parseInt(endDate as string)} })
            .limit(10)
            .toArray();

        res.status(200).json(posts);
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Internal Server Error' });
    }
}