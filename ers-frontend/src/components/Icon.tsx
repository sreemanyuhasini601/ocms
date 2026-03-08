import { createElement, type ComponentType } from 'react';
import type { IconType } from 'react-icons';

interface IconProps {
  icon: IconType;
  className?: string;
}

/** Wrapper for react-icons to satisfy React 18 / TypeScript JSX typing */
export function Icon({ icon, className }: IconProps): JSX.Element {
  return createElement(icon as ComponentType<{ className?: string }>, { className });
}
