import { NextApiRequest, NextApiResponse } from 'next';
import { ObjectId } from 'mongodb';
import clientPromise from '@/lib/mongodb';

export default async function findPostById(req: NextApiRequest, res: NextApiResponse) {
    const { id } = req.query;

    if (!ObjectId.isValid(id as string)) {
        res.status(400).json({ message: 'Invalid ID format' });
        return;
    }

    try {
        const client = await clientPromise;
        const db = client.db('thesis');

        const post = await db
            .collection<any>('article')
            .findOne({ _id: new ObjectId(id as string) });

        if (!post) {
            res.status(404).json({ message: 'Post not found' });
            return;
        }

        res.status(200).json(post);
    } catch (e) {
        console.error(e);
        res.status(500).json({ message: 'Internal Server Error' });
    }
};
