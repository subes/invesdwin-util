package de.invesdwin.util.math.expression.eval;

import javax.annotation.concurrent.Immutable;

import de.invesdwin.util.math.expression.ExpressionType;
import de.invesdwin.util.math.expression.IExpression;
import de.invesdwin.util.math.expression.IFunction;
import de.invesdwin.util.math.expression.IFunctionParameterInfo;
import de.invesdwin.util.time.fdate.FDate;

@Immutable
public final class Functions {

    public static final IFunction SIN = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.sin(a);
        }

        @Override
        public String getExpressionName() {
            return "sin";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "a";
                }

                @Override
                public String getDescription() {
                    return "An angle, in radians.";
                }
            };
        }

        @Override
        public String getName() {
            return "Trigonometric Sine";
        }

        @Override
        public String getDescription() {
            return "Returns the trigonometric sine of an angle.";
        }
    };

    public static final IFunction SINH = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.sinh(a);
        }

        @Override
        public String getExpressionName() {
            return "sinh";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "x";
                }

                @Override
                public String getDescription() {
                    return "The number whose hyperbolic sine is to be returned.";
                }
            };
        }

        @Override
        public String getName() {
            return "Hyperbolic Sine";
        }

        @Override
        public String getDescription() {
            return "Returns the hyperbolic sine of an angle. "
                    + "The hyperbolic sine of x is defined to be (ex - e-x)/2 where e is Euler's number.";
        }
    };

    public static final IFunction COS = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.cos(a);
        }

        @Override
        public String getExpressionName() {
            return "cos";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "a";
                }

                @Override
                public String getDescription() {
                    return "An angle, in radians.";
                }
            };
        }

        @Override
        public String getName() {
            return "Trignonometric Cosine";
        }

        @Override
        public String getDescription() {
            return "Returns the trigonometric cosine of an angle.";
        }
    };

    public static final IFunction COSH = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.cosh(a);
        }

        @Override
        public String getExpressionName() {
            return "cosh";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "x";
                }

                @Override
                public String getDescription() {
                    return "The number whose hyperbolic cosine is to be returned.";
                }
            };
        }

        @Override
        public String getName() {
            return "Hyperbolic Cosine";
        }

        @Override
        public String getDescription() {
            return "Returns the hyperbolic cosine of a double value. "
                    + "The hyperbolic cosine of x is defined to be (ex + e-x)/2 where e is Euler's number.";
        }
    };

    public static final IFunction TAN = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.tan(a);
        }

        @Override
        public String getExpressionName() {
            return "tan";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "a";
                }

                @Override
                public String getDescription() {
                    return "An angle, in radians.";
                }
            };
        }

        @Override
        public String getName() {
            return "Trigonometric Tangent";
        }

        @Override
        public String getDescription() {
            return "Returns the trigonometric tangent of an angle.";
        }
    };

    public static final IFunction TANH = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.tanh(a);
        }

        @Override
        public String getExpressionName() {
            return "tanh";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "x";
                }

                @Override
                public String getDescription() {
                    return "The number whose hyperbolic tangent is to be returned.";
                }
            };
        }

        @Override
        public String getName() {
            return "Hyperbolic Tangent";
        }

        @Override
        public String getDescription() {
            return "Returns the hyperbolic tangent of a double value. "
                    + "The hyperbolic tangent of x is defined to be (ex - e-x)/(ex + e-x), in other words, sinh(x)/cosh(x). "
                    + "Note that the absolute value of the exact tanh is always less than 1.";
        }
    };

    public static final IFunction ABS = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.abs(a);
        }

        @Override
        public String getExpressionName() {
            return "abs";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "a";
                }

                @Override
                public String getDescription() {
                    return "The argument whose absolute value is to be determined.";
                }
            };
        }

        @Override
        public String getName() {
            return "Absolute";
        }

        @Override
        public String getDescription() {
            return "Returns the absolute value of a double value. If the argument is not negative, the argument is returned. "
                    + "If the argument is negative, the negation of the argument is returned.";
        }
    };

    public static final IFunction ASIN = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.asin(a);
        }

        @Override
        public String getExpressionName() {
            return "asin";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "a";
                }

                @Override
                public String getDescription() {
                    return "The value whose arc sine is to be returned.";
                }
            };
        }

        @Override
        public String getName() {
            return "Arc Sine";
        }

        @Override
        public String getDescription() {
            return "Returns the arc sine of a value; the returned angle is in the range -pi/2 through pi/2.";
        }
    };

    public static final IFunction ACOS = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.acos(a);
        }

        @Override
        public String getExpressionName() {
            return "acos";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "a";
                }

                @Override
                public String getDescription() {
                    return "The value whose arc cosine is to be returned.";
                }
            };
        }

        @Override
        public String getName() {
            return "Arc Cosine";
        }

        @Override
        public String getDescription() {
            return "Returns the arc cosine of a value; the returned angle is in the range 0.0 through pi.";
        }
    };

    public static final IFunction ATAN = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.atan(a);
        }

        @Override
        public String getExpressionName() {
            return "atan";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "a";
                }

                @Override
                public String getDescription() {
                    return "The value whose arc tangent is to be returned.";
                }
            };
        }

        @Override
        public String getName() {
            return "Arc Tangent";
        }

        @Override
        public String getDescription() {
            return "Returns the arc tangent of a value; the returned angle is in the range -pi/2 through pi/2.";
        }
    };

    public static final IFunction ATAN2 = new ABinaryFunction() {
        @Override
        protected double eval(final double a, final double b) {
            return Math.atan2(a, b);
        }

        @Override
        public String getExpressionName() {
            return "atan2";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            switch (index) {
            case 0:
                return new IFunctionParameterInfo() {

                    @Override
                    public String getType() {
                        return ExpressionType.Double.toString();
                    }

                    @Override
                    public String getName() {
                        return "y";
                    }

                    @Override
                    public String getDescription() {
                        return "The ordinate coordinate.";
                    }
                };
            case 1:
                return new IFunctionParameterInfo() {

                    @Override
                    public String getType() {
                        return ExpressionType.Double.toString();
                    }

                    @Override
                    public String getName() {
                        return "x";
                    }

                    @Override
                    public String getDescription() {
                        return "The abscissa coordinate.";
                    }
                };
            default:
                throw new ArrayIndexOutOfBoundsException(index);
            }
        }

        @Override
        public String getName() {
            return "Angle Theta";
        }

        @Override
        public String getDescription() {
            return "Returns the angle theta from the conversion of rectangular coordinates (x, y) to polar coordinates (r, theta). "
                    + "This method computes the phase theta by computing an arc tangent of y/x in the range of -pi to pi.";
        }
    };

    public static final IFunction ROUND = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.round(a);
        }

        @Override
        public String getExpressionName() {
            return "round";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "a";
                }

                @Override
                public String getDescription() {
                    return "A floating-point value to be rounded to a long.";
                }
            };
        }

        @Override
        public String getName() {
            return "Round";
        }

        @Override
        public String getDescription() {
            return "Returns the closest long to the argument, with ties rounding to positive infinity.";
        }
    };

    public static final IFunction FLOOR = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.floor(a);
        }

        @Override
        public String getExpressionName() {
            return "floor";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "a";
                }

                @Override
                public String getDescription() {
                    return "A value.";
                }
            };
        }

        @Override
        public String getName() {
            return "Floor";
        }

        @Override
        public String getDescription() {
            return "Returns the largest (closest to positive infinity) double value that is less than "
                    + "or equal to the argument and is equal to a mathematical integer.";
        }
    };

    public static final IFunction CEIL = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.ceil(a);
        }

        @Override
        public String getExpressionName() {
            return "ceil";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "a";
                }

                @Override
                public String getDescription() {
                    return "A value.";
                }
            };
        }

        @Override
        public String getName() {
            return "Ceiling";
        }

        @Override
        public String getDescription() {
            return "Returns the smallest (closest to negative infinity) double value that is greater than "
                    + "or equal to the argument and is equal to a mathematical integer.";
        }
    };

    public static final IFunction POW = new ABinaryFunction() {
        @Override
        protected double eval(final double a, final double b) {
            return Math.pow(a, b);
        }

        @Override
        public String getExpressionName() {
            return "pow";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            switch (index) {
            case 0:
                return new IFunctionParameterInfo() {

                    @Override
                    public String getType() {
                        return ExpressionType.Double.toString();
                    }

                    @Override
                    public String getName() {
                        return "a";
                    }

                    @Override
                    public String getDescription() {
                        return "The base.";
                    }
                };
            case 1:
                return new IFunctionParameterInfo() {

                    @Override
                    public String getType() {
                        return ExpressionType.Double.toString();
                    }

                    @Override
                    public String getName() {
                        return "b";
                    }

                    @Override
                    public String getDescription() {
                        return "The exponent.";
                    }
                };
            default:
                throw new ArrayIndexOutOfBoundsException(index);
            }
        }

        @Override
        public String getName() {
            return "Power";
        }

        @Override
        public String getDescription() {
            return "Returns the value of the first argument raised to the power of the second argument.";
        }
    };

    public static final IFunction SQRT = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.sqrt(a);
        }

        @Override
        public String getExpressionName() {
            return "sqrt";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "a";
                }

                @Override
                public String getDescription() {
                    return "A value.";
                }
            };
        }

        @Override
        public String getName() {
            return "Square Root";
        }

        @Override
        public String getDescription() {
            return "Returns the correctly rounded positive square root of a double value.";
        }
    };

    public static final IFunction EXP = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.exp(a);
        }

        @Override
        public String getExpressionName() {
            return "exp";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "a";
                }

                @Override
                public String getDescription() {
                    return "The exponent to raise e to.";
                }
            };
        }

        @Override
        public String getName() {
            return "Natural Exponential";
        }

        @Override
        public String getDescription() {
            return "Returns Euler's number e raised to the power of a double value.";
        }
    };

    public static final IFunction LN = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.log(a);
        }

        @Override
        public String getExpressionName() {
            return "ln";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "a";
                }

                @Override
                public String getDescription() {
                    return "A value.";
                }
            };
        }

        @Override
        public String getName() {
            return "Natural Logarithm";
        }

        @Override
        public String getDescription() {
            return "Returns the natural logarithm (base e) of a double value.";
        }
    };

    public static final IFunction LOG = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.log10(a);
        }

        @Override
        public String getExpressionName() {
            return "log";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "a";
                }

                @Override
                public String getDescription() {
                    return "A value.";
                }
            };
        }

        @Override
        public String getName() {
            return "Base 10 Logarithm";
        }

        @Override
        public String getDescription() {
            return "Returns the base 10 logarithm of a double value.";
        }
    };

    public static final IFunction MIN = new ABinaryFunction() {
        @Override
        protected double eval(final double a, final double b) {
            return Math.min(a, b);
        }

        @Override
        public String getExpressionName() {
            return "min";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            switch (index) {
            case 0:
                return new IFunctionParameterInfo() {

                    @Override
                    public String getType() {
                        return ExpressionType.Double.toString();
                    }

                    @Override
                    public String getName() {
                        return "a";
                    }

                    @Override
                    public String getDescription() {
                        return "An argument.";
                    }
                };
            case 1:
                return new IFunctionParameterInfo() {

                    @Override
                    public String getType() {
                        return ExpressionType.Double.toString();
                    }

                    @Override
                    public String getName() {
                        return "b";
                    }

                    @Override
                    public String getDescription() {
                        return "Another agument.";
                    }
                };
            default:
                throw new ArrayIndexOutOfBoundsException(index);
            }
        }

        @Override
        public String getName() {
            return "Minimum";
        }

        @Override
        public String getDescription() {
            return "Returns the smaller of two double values. That is, the result is the value closer to negative infinity. "
                    + "If the arguments have the same value, the result is that same value. If either value is NaN, then the result is NaN. "
                    + "Unlike the numerical comparison operators, this method considers negative zero to be strictly smaller than positive zero. "
                    + "If one argument is positive zero and the other is negative zero, the result is negative zero.";
        }
    };

    public static final IFunction MAX = new ABinaryFunction() {
        @Override
        protected double eval(final double a, final double b) {
            return Math.max(a, b);
        }

        @Override
        public String getExpressionName() {
            return "max";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            switch (index) {
            case 0:
                return new IFunctionParameterInfo() {

                    @Override
                    public String getType() {
                        return ExpressionType.Double.toString();
                    }

                    @Override
                    public String getName() {
                        return "a";
                    }

                    @Override
                    public String getDescription() {
                        return "An argument.";
                    }
                };
            case 1:
                return new IFunctionParameterInfo() {

                    @Override
                    public String getType() {
                        return ExpressionType.Double.toString();
                    }

                    @Override
                    public String getName() {
                        return "b";
                    }

                    @Override
                    public String getDescription() {
                        return "Another agument.";
                    }
                };
            default:
                throw new ArrayIndexOutOfBoundsException(index);
            }
        }

        @Override
        public String getName() {
            return "Maxiimum";
        }

        @Override
        public String getDescription() {
            return "Returns the greater of two double values. That is, the result is the argument closer to positive infinity. "
                    + "If the arguments have the same value, the result is that same value. If either value is NaN, then the result is NaN. "
                    + "Unlike the numerical comparison operators, this method considers negative zero to be strictly smaller than positive zero. "
                    + "If one argument is positive zero and the other negative zero, the result is positive zero.";
        }
    };

    public static final IFunction RND = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.random() * a;
        }

        @Override
        public String getExpressionName() {
            return "rnd";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "a";
                }

                @Override
                public String getDescription() {
                    return "A value to multiply the random value with.";
                }
            };
        }

        @Override
        public String getName() {
            return "Random";
        }

        @Override
        public String getDescription() {
            return "Returns a double value with a positive sign, greater than or equal to 0.0 and less than 1.0. This value is multiplied by the argument.";
        }
    };

    public static final IFunction SIGN = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.signum(a);
        }

        @Override
        public String getExpressionName() {
            return "sign";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "d";
                }

                @Override
                public String getDescription() {
                    return "The floating-point value whose signum is to be returned.";
                }
            };
        }

        @Override
        public String getName() {
            return "Signum";
        }

        @Override
        public String getDescription() {
            return "Returns the signum function of the argument; zero if the argument is zero, "
                    + "1.0 if the argument is greater than zero, -1.0 if the argument is less than zero.";
        }
    };

    public static final IFunction DEG = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.toDegrees(a);
        }

        @Override
        public String getExpressionName() {
            return "deg";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "angrad";
                }

                @Override
                public String getDescription() {
                    return "An angle, in radians.";
                }
            };
        }

        @Override
        public String getName() {
            return "Degrees";
        }

        @Override
        public String getDescription() {
            return "Converts an angle measured in radians to an approximately equivalent angle measured in degrees. "
                    + "The conversion from radians to degrees is generally inexact; users should not expect cos(rad(90.0)) to exactly equal 0.0.";
        }
    };

    public static final IFunction RAD = new AUnaryFunction() {
        @Override
        protected double eval(final double a) {
            return Math.toRadians(a);
        }

        @Override
        public String getExpressionName() {
            return "rad";
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "angdeg";
                }

                @Override
                public String getDescription() {
                    return "An angle, in degrees.";
                }
            };
        }

        @Override
        public String getName() {
            return "Radians";
        }

        @Override
        public String getDescription() {
            return "Converts an angle measured in degrees to an approximately equivalent angle measured in radians. "
                    + "The conversion from degrees to radians is generally inexact.";
        }
    };

    public static final IFunction IF = new IFunction() {

        @Override
        public String getExpressionName() {
            return "if";
        }

        @Override
        public int getNumberOfArguments() {
            return 3;
        }

        @Override
        public double eval(final FDate key, final IExpression[] args) {
            final boolean check = args[0].evaluateBoolean(key);
            if (check) {
                return args[1].evaluateDouble(key);
            } else {
                return args[2].evaluateDouble(key);
            }
        }

        @Override
        public double eval(final int key, final IExpression[] args) {
            final boolean check = args[0].evaluateBoolean(key);
            if (check) {
                return args[1].evaluateDouble(key);
            } else {
                return args[2].evaluateDouble(key);
            }
        }

        @Override
        public double eval(final IExpression[] args) {
            final boolean check = args[0].evaluateBoolean();
            if (check) {
                return args[1].evaluateDouble();
            } else {
                return args[2].evaluateDouble();
            }
        }

        @Override
        public boolean isNaturalFunction() {
            return false;
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Double;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            switch (index) {
            case 0:
                return new IFunctionParameterInfo() {

                    @Override
                    public String getType() {
                        return ExpressionType.Boolean.toString();
                    }

                    @Override
                    public String getName() {
                        return "condition";
                    }

                    @Override
                    public String getDescription() {
                        return "The boolean expression to evaluate. A value greater than 0 means true.";
                    }
                };
            case 1:
                return new IFunctionParameterInfo() {

                    @Override
                    public String getType() {
                        return ExpressionType.Double.toString();
                    }

                    @Override
                    public String getName() {
                        return "then";
                    }

                    @Override
                    public String getDescription() {
                        return "The return value when the if condition is true.";
                    }
                };
            case 2:
                return new IFunctionParameterInfo() {

                    @Override
                    public String getType() {
                        return ExpressionType.Double.toString();
                    }

                    @Override
                    public String getName() {
                        return "else";
                    }

                    @Override
                    public String getDescription() {
                        return "The return value when the if condition is not true.";
                    }
                };
            default:
                throw new ArrayIndexOutOfBoundsException(index);
            }
        }

        @Override
        public String getName() {
            return "If Condition-Then-Else";
        }

        @Override
        public String getDescription() {
            return "If the conditional evaluation is true, then the second argument is returned, otherwise the third argument is returned.";
        }
    };

    public static final IFunction ISNAN = new IFunction() {

        @Override
        public String getExpressionName() {
            return "isNaN";
        }

        @Override
        public boolean isNaturalFunction() {
            return true;
        }

        @Override
        public int getNumberOfArguments() {
            return 1;
        }

        @Override
        public double eval(final FDate key, final IExpression[] args) {
            final double a = args[0].evaluateDouble(key);
            if (Double.isNaN(a)) {
                return 1D;
            } else {
                return 0D;
            }
        }

        @Override
        public double eval(final int key, final IExpression[] args) {
            final double a = args[0].evaluateDouble(key);
            if (Double.isNaN(a)) {
                return 1D;
            } else {
                return 0D;
            }
        }

        @Override
        public double eval(final IExpression[] args) {
            final double a = args[0].evaluateDouble();
            if (Double.isNaN(a)) {
                return 1D;
            } else {
                return 0D;
            }
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Boolean;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "a";
                }

                @Override
                public String getDescription() {
                    return "A value.";
                }
            };
        }

        @Override
        public String getName() {
            return "Is Not a Number (NaN)";
        }

        @Override
        public String getDescription() {
            return "Evaluates to true when the argument is equal to NaN which denotes a missing value.";
        }
    };

    public static final IFunction ISTRUE = new IFunction() {

        @Override
        public String getExpressionName() {
            return "isTrue";
        }

        @Override
        public boolean isNaturalFunction() {
            return true;
        }

        @Override
        public int getNumberOfArguments() {
            return 1;
        }

        @Override
        public double eval(final FDate key, final IExpression[] args) {
            final boolean a = args[0].evaluateBoolean(key);
            if (a) {
                return 1D;
            } else {
                return 0D;
            }
        }

        @Override
        public double eval(final int key, final IExpression[] args) {
            final boolean a = args[0].evaluateBoolean(key);
            if (a) {
                return 1D;
            } else {
                return 0D;
            }
        }

        @Override
        public double eval(final IExpression[] args) {
            final boolean a = args[0].evaluateBoolean();
            if (a) {
                return 1D;
            } else {
                return 0D;
            }
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Boolean;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "condition";
                }

                @Override
                public String getDescription() {
                    return "The boolean expression to evaluate.";
                }
            };
        }

        @Override
        public String getName() {
            return "Is True";
        }

        @Override
        public String getDescription() {
            return "A value greater than 0 means true.";
        }
    };

    public static final IFunction ISFALSE = new IFunction() {

        @Override
        public String getExpressionName() {
            return "isFalse";
        }

        @Override
        public boolean isNaturalFunction() {
            return true;
        }

        @Override
        public int getNumberOfArguments() {
            return 1;
        }

        @Override
        public double eval(final FDate key, final IExpression[] args) {
            final boolean a = args[0].evaluateBoolean(key);
            if (a) {
                return 0D;
            } else {
                return 1D;
            }
        }

        @Override
        public double eval(final int key, final IExpression[] args) {
            final boolean a = args[0].evaluateBoolean(key);
            if (a) {
                return 0D;
            } else {
                return 1D;
            }
        }

        @Override
        public double eval(final IExpression[] args) {
            final boolean a = args[0].evaluateBoolean();
            if (a) {
                return 0D;
            } else {
                return 1D;
            }
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Boolean;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "condition";
                }

                @Override
                public String getDescription() {
                    return "The boolean expression to evaluate.";
                }
            };
        }

        @Override
        public String getName() {
            return "Is False";
        }

        @Override
        public String getDescription() {
            return "A value less than or equal to 0 means true.";
        }
    };

    public static final IFunction NEGATE = new IFunction() {

        @Override
        public String getExpressionName() {
            return "negate";
        }

        @Override
        public boolean isNaturalFunction() {
            return true;
        }

        @Override
        public int getNumberOfArguments() {
            return 1;
        }

        @Override
        public double eval(final FDate key, final IExpression[] args) {
            final double a = args[0].evaluateDouble(key);
            return -a;
        }

        @Override
        public double eval(final int key, final IExpression[] args) {
            final double a = args[0].evaluateDouble(key);
            return -a;
        }

        @Override
        public double eval(final IExpression[] args) {
            final double a = args[0].evaluateDouble();
            return -a;
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Boolean;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "v";
                }

                @Override
                public String getDescription() {
                    return "The value to negate.";
                }
            };
        }

        @Override
        public String getName() {
            return "Numerical Negation";
        }

        @Override
        public String getDescription() {
            return "A positive number will become negative, a negative value will become positive. 0 will stay 0.";
        }
    };

    public static final IFunction NOT = new IFunction() {

        @Override
        public String getExpressionName() {
            return "not";
        }

        @Override
        public boolean isNaturalFunction() {
            return true;
        }

        @Override
        public int getNumberOfArguments() {
            return 1;
        }

        @Override
        public double eval(final FDate key, final IExpression[] args) {
            final boolean a = args[0].evaluateBoolean(key);
            if (a) {
                return 0D;
            } else {
                return 1D;
            }
        }

        @Override
        public double eval(final int key, final IExpression[] args) {
            final boolean a = args[0].evaluateBoolean(key);
            if (a) {
                return 0D;
            } else {
                return 1D;
            }
        }

        @Override
        public double eval(final IExpression[] args) {
            final boolean a = args[0].evaluateBoolean();
            if (a) {
                return 0D;
            } else {
                return 1D;
            }
        }

        @Override
        public ExpressionType getReturnType() {
            return ExpressionType.Boolean;
        }

        @Override
        public IFunctionParameterInfo getParameterInfo(final int index) {
            if (index != 0) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return new IFunctionParameterInfo() {

                @Override
                public String getType() {
                    return ExpressionType.Double.toString();
                }

                @Override
                public String getName() {
                    return "condition";
                }

                @Override
                public String getDescription() {
                    return "The boolean expression to negate.";
                }
            };
        }

        @Override
        public String getName() {
            return "Boolean Negation (Not)";
        }

        @Override
        public String getDescription() {
            return "True will become false and false will become true.";
        }
    };

    private Functions() {}

}
