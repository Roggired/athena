import * as path from 'path'
import { ResolveOptions } from 'webpack'
import { src } from '../shared/paths'

const alias = (name: string): string => path.resolve(src, name)

export const resolve: ResolveOptions = {
    extensions: ['.tsx', '.ts', '.json', '.js'],
    alias: {
        '@shared': alias('shared'),
        '@entities': alias('entities'),
        '@features': alias('features'),
        '@assets': alias('assets'),
        '@pages': alias('pages'),
        '@app': alias('app'),
    },
}
