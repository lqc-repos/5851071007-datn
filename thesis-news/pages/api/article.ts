import clientPromise from '@/lib/mongodb';
import { NextApiRequest, NextApiResponse } from 'next';

export default async function findPost(req: NextApiRequest, res: NextApiResponse) {
    try {
        const { page = 1 } = req.query
        const client = await clientPromise;
        const db = client.db('thesis');
        const posts = await db
            .collection<any>('article')
            .find({})
            .limit(10)
            .skip(10 * (parseInt(page as string) - 1))
            .toArray();


        res.status(200).json(posts);
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Internal Server Error' });
    }
}