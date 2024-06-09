import clientPromise from '@/lib/mongodb';
import { NextApiRequest, NextApiResponse } from 'next';

export const findPost = async (req: NextApiRequest, res: NextApiResponse) => {
    try {
        const client = await clientPromise;
        const db = client.db('thesis');
        const posts = await db
            .collection<any>('article')
            .find({})
            .toArray();

        const resp = res.status(200).json(posts);
        console.log(resp);
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Internal Server Error' });
    }
}